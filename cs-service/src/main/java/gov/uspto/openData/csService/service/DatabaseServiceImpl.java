package gov.uspto.openData.csService.service;

import gov.uspto.openData.csService.model.FileArchive;
import gov.uspto.openData.csService.model.FileData;
import gov.uspto.openData.csService.model.FileVersion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseServiceImpl implements DatabaseService {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public FileData saveFileMetadata(FileData fileData){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int updateResult = jdbcTemplate.update("insert into FILE_METADATA (storage_id, file_name, application_name, content_length, content_type, created_date) "
					+ " values (:storageId, :fileName, :applicationName, :contentLength, :contentType, :createdDate)", 
				new BeanPropertySqlParameterSource(fileData), keyHolder);
		Long fileId = (Long)keyHolder.getKey();
		fileData.setFileId(fileId);
		return fileData;
	}
	
	public FileData getFileMetadataById(Long fileId){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("fileId", fileId);
		List<FileData> fileDataList = jdbcTemplate.query("select * from FILE_METADATA fm where fm.file_id = :fileId", 
					parameterMap, 
					BeanPropertyRowMapper.newInstance(FileData.class));
		
		if(fileDataList == null || fileDataList.size() == 0){
			return null;
		}
		
		return fileDataList.get(0);
	}
	
	public FileVersion createFileVersion(FileVersion fileVersion){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int updateResult = jdbcTemplate.update("insert into FILE_VERSION (file_id, storage_id, file_name, application_name, content_length, content_type, file_created_date, created_date) "
					+ " values (:fileId, :storageId, :fileName, :applicationName, :contentLength, :contentType, :fileCreatedDate, :createdDate)", 
				new BeanPropertySqlParameterSource(fileVersion), keyHolder);
		Long versionId = (Long)keyHolder.getKey();
		fileVersion.setVersionId(versionId);
		return fileVersion;
	}
	
	public FileData updateFileMetadata(FileData fileData){
		int updateResult = jdbcTemplate.update("update FILE_METADATA set storage_id = :storageId, file_name = :fileName, "
											+ " application_name = :applicationName, content_length = :contentLength, content_type = :contentType, created_date = :createdDate "
											+ " where file_id = :fileId ", new BeanPropertySqlParameterSource(fileData));
		return fileData;
	}
	
	public FileArchive createFileArchive(FileArchive fileArchive){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int updateResult = jdbcTemplate.update("insert into FILE_ARCHIVE (file_id, storage_id, file_name, application_name, content_length, content_type, file_created_date, created_date) "
					+ " values (:fileId, :storageId, :fileName, :applicationName, :contentLength, :contentType, :fileCreatedDate, :createdDate)", 
				new BeanPropertySqlParameterSource(fileArchive), keyHolder);
		Long archiveId = (Long)keyHolder.getKey();
		fileArchive.setArchiveId(archiveId);
		return fileArchive;
	}
	
	public void copyFileMetadataToArchive(Long fileId){
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fileId", fileId);
		int updateResult =  jdbcTemplate.update("insert into FILE_ARCHIVE(file_id, storage_id, file_name, application_name, content_length, content_type, file_created_date, created_date)"
											+ " select file_id, storage_id, file_name, application_name, content_length, content_type, created_date, current_timestamp from FILE_METADATA where file_id = :fileId ", paramSource);
	}
	
	public void copyFileVersionsToArchive(Long fileId){
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fileId", fileId);
		int updateResult =  jdbcTemplate.update("insert into FILE_ARCHIVE(file_id, storage_id, file_name, application_name, content_length, content_type, file_created_date, created_date)"
											+ " select file_id, storage_id, file_name, application_name, content_length, content_type, file_created_date, current_timestamp from FILE_VERSION where file_id = :fileId ", paramSource);
	}
	
	public void deleteFileMetadata(Long fileId){
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fileId", fileId);
		jdbcTemplate.update("delete from FILE_METADATA where file_id = :fileId", paramSource);
	}
	
	public void deleteFileVersions(Long fileId){
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fileId", fileId);
		jdbcTemplate.update("delete from FILE_VERSION where file_id = :fileId", paramSource);
	}
	

}
