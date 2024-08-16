package com.courseend.zumba.service;

import java.util.List;

import com.courseend.zumba.dao.BatchDAO;
import com.courseend.zumba.model.Batch;

public class BatchService implements IBatchService {

	private BatchDAO batchDAO;

	public BatchService(BatchDAO batchDAO) {
		this.setBatchDAO(batchDAO);
	}

	@Override
	public void addBatch(Batch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBatch(Batch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBatch(int batchId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Batch getBatch(int batchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Batch> getAllBatches() {
		// TODO Auto-generated method stub
		return null;
	}

	public BatchDAO getBatchDAO() {
		return batchDAO;
	}

	public void setBatchDAO(BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}

}