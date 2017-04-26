podTemplate(label: 'maven-ose', cloud: 'openshift', containers: [
  containerTemplate(name: 'maven', image: "registry.access.redhat.com/openshift3/jenkins-slave-maven-rhel7", ttyEnabled: true, command: 'cat', workingDir: '/home/jenkins'),
],
volumes: [configMapVolume(configMapName: 'jenkins-maven-settings', mountPath: '/etc/maven'),
          secretVolume(secretName: 'jenkins-nepemail-token-bfxfb', mountPath: '/etc/jenkins'),
          persistentVolumeClaim(claimName: 'maven-local-repo', mountPath: '/etc/.m2repo')]) {

    	node('maven-ose') {
        	container(name: 'maven', cloud: 'openshift') {

	def appName = "content-store"
	def devProject = "content-store-int"
	//def uatProject = "api-app-uat"
	//def prodProject = "api-app-prod"
	def version

    	def WORKSPACE = pwd()

        //def mvnHome = tool 'maven'
        env.KUBECONFIG = pwd() + "/.kubeconfig"

        stage 'Checkout'

                checkout scm


        stage "OpenShift Dev Build"
		 
		version = parseVersion("${WORKSPACE}/pom.xml")
		
		echo version

                login()

                sh """
                  set -x

                  currentOutputName=\$(oc get bc ${appName} -n ${devProject} --template='{{ .spec.output.to.name }}')

                  newImageName=\${currentOutputName%:*}:${version}

                  oc patch bc ${appName} -n ${devProject} -p "{ \\"spec\\": { \\"output\\": { \\"to\\": { \\"name\\": \\"\${newImageName}\\" } } } }"


                  oc start-build ${appName} -n ${devProject} --follow=true --wait=true --from-file=./cs-rest/target/cs-rest.war

                """

        stage "Dev Deployment"

                login()

                deployApp(appName, devProject, version)

                validateDeployment(appName,devProject)

        }


    }
   
}

def processStageResult() {

    if (currentBuild.result != null) {
        sh "exit 1"
    }
}

def login() {
    sh """
       set +x
       oc login --certificate-authority=/var/run/secrets/kubernetes.io/serviceaccount/ca.crt --token=\$(cat /var/run/secrets/kubernetes.io/serviceaccount/token) https://kubernetes.default.svc.cluster.local >/dev/null 2>&1 || echo 'OpenShift login failed'
       """
}

def parseVersion(String filename) {
  def matcher = readFile(filename) =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}

def deployApp(appName, namespace, version) {
       sh """
          set -x

          newDeploymentImageName=${appName}:${version}

          imageReference=\$(oc get is ${appName} -n ${namespace} -o jsonpath="{.status.tags[?(@.tag==\\"${version}\\")].items[*].dockerImageReference}")

          oc patch dc/${appName} -n ${namespace} -p "{\\"spec\\":{\\"template\\":{\\"spec\\":{\\"containers\\":[{\\"name\\":\\"${appName}\\",\\"image\\": \\"\${imageReference}\\" } ]}}, \\"triggers\\": [ { \\"type\\": \\"ImageChange\\", \\"imageChangeParams\\": { \\"containerNames\\": [ \\"${appName}\\" ], \\"from\\": { \\"kind\\": \\"ImageStreamTag\\", \\"name\\": \\"\${newDeploymentImageName}\\" } } } ] }}"

          oc rollout latest dc/${appName} -n ${namespace}

          # Sleep for a few moments
          sleep 5
       """


}

def acceptanceCheck(String appName, String namespace) {

    sh """
      set -x

      COUNTER=0
      DELAY=5
      MAX_COUNTER=30

      echo "Running Acceptance Check of ${appName} in project ${namespace}"

     set +e

      while [ \$COUNTER -lt \$MAX_COUNTER ]
      do

        RESPONSE=\$(curl -s -o /dev/null -w '%{http_code}\\n' http://${appName}.${namespace}.cloudapps.rhc-lab.iad.redhat.com/rest/api/pods)

        if [ \$RESPONSE -eq 200 ]; then
            echo
            echo "Application Verified"
            break
        fi

        if [ \$COUNTER -eq \$MAX_COUNTER ]; then
          echo "Max Validation Attempts Exceeded. Failed Verifying Application Deployment..."
          exit 1
        fi

        sleep \$DELAY

      done

      set -e
      """

}


/* Promotes an image from one namespace to another by tagging the image in the new namespace.
 *
 * @param imageStreamNames [Array]  ImageStream names to promote
 * @param srcNamespace     [String] Source namspace to promote the ImageStreams from.
 * @param destNamespace    [String] Destination namespace to promote the ImageStreams to.
 * @param version          [String] Version of the ImageStreams to promote.
 */
def promote(imageStreamNames, srcNamespace, destNamespace, version) {
    for ( is in imageStreamNames ) {
        def imageStream = is //if you use 'an' below, it will equal the last value in each parallel run

        sh """
          set -x
          oc tag ${srcNamespace}/${imageStream}:${version} ${destNamespace}/${imageStream}:${version} --alias=false --loglevel=${OC_LOG_LEVEL}
        """
    }
}


