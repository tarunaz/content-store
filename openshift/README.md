Binary Build Scripts
=

This directory houses a number of build scripts that can be run to assist with OpenShift housekeeping tasks for the content-store project, such as creation of a Developer project from source, creation of a Jenkins managed pipeline to promote from Integration to UAT and Production, deleting a pipeline, as well as initializing a MySQL Database pod with data.

**content-store-create-developer-project**
-

 -*What it does:*

This script is responsible for creating an OpenShift project for a developer on the team. It takes in one parameter, the developer name. And it outputs the Webhook URL that the developer can set on the Git project so that Jenkins will automatically kick off a new build on a new commit.
- It preforms the following tasks
 - It creates a new OpenShift project using the format content-store-dev-${DEV_NAME}
 - Creates a server.crt and server.key for the jws-app-secret needed by the tomcat8 template, using openssl
 - Processes the USPTO tomcat8 developer template with the required parameters and creates the OCP resources. The developer template also has a Source BuildConfig that gets created, to build from source.
 - Initializes the MySQL database pod with data, using the content-store-init-ocp-mysql-db script in the same folder

 -*How to use it:*

With the project cloned locally, once inside this directory you can run this script with 

	./content-store-create-developer-project [DEV_NAME]

The value of [DEV_NAME] is the developer name that will work on the project in OpenShift and have his/her own forked repository / branch in Git.

**content-store-create-pipeline**
-

 -*What it does:*

This script is responsible for creating the OpenShift environment for the promotion pipeline to operate in. It will create three projects content-store-int, content-store-uat and content-store-prod and also a content-store-pipeline BuildConfig in the CICD project that executes the pipeline in Jenkins.
- Key items 
 - Processes the uspto-tomcat8-runtime-ephemeral-template for the content-store-int and content-store-uat projects and uspto-tomcat8-runtime-persistent-template for the content-store-prod project (to use persistent MySQL for the production environment)
 - Performs the same tasks for each of the three projects that it does for the developer project i.e create a new OpenShift project, a server.crt and server.key, process the runtime template to create the OCP resources, and initialize the MySQL database.
 - Creates a Binary BuildConfig in the content-store-int project that is used to trigger a Binary build in the Integration environment using the war artifact built in Jenkins
 - The content-store-uat and content-store-prod projects do not have a BuildConfig as the project is built once in the Integration environment and deployed everywhere
 - The Integration and UAT projects are scaled to 2 pods and Production is scaled to 4 for HA

 -*How to use it:*

Simply running

	./content-store-create-pipeline

will create the Jenkins pipeline to promote from Integration to Production in OpenShift. 

**content-store-delete-pipeline**
-

 -*What it does:*

This script deletes all the OCP resources created for the pipeline in OpenShift. It deletes the three Integration, UAT and Production projects and the content-store-pipeline BuildConfig.

 -*How to use it:*

Simply running

	./content-store-delete-pipeline

will delete the projects and pipeline in OpenShift

**content-store-init-ocp-mysql-db**
-

 -*What it does:*

This script is responsible for identifying and instantiating the database for the content-store application. It takes in two input parameters, the username and password for the MySQL database.
- It performs the following steps
 - Identify the MySQL pod that is running for the content-store application
 - RSYNC SQL data files from /data/sql folder in the project to the /var/lib/mysql/data folder in the running MySQL pod
 - RSH into the pod and source the copied V1__version.sql file as root user

 -*How to use it:*

After running content-store-create-developer-project, you can run this script with

	./content-init-ocp-mysql-db [DB_USER] [DB_PASSWORD]

The value of [DB\_USER] and [DB\_PASSWORD] are both provided within the OpenShift web UI after running content-store-create-developer-project.
