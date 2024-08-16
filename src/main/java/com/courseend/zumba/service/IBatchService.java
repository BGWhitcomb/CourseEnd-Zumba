package com.courseend.zumba.service;

import java.util.List;

import com.courseend.zumba.model.Batch;

public interface IBatchService {
	void addBatch(Batch batch);

	void updateBatch(Batch batch);

	void deleteBatch(int batchId);

	Batch getBatch(int batchId);

	List<Batch> getAllBatches();
}

