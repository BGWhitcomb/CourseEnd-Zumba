package com.courseend.zumba.dao;

import java.util.List;

import com.courseend.zumba.model.Batch;

public interface IBatchDAO {
	void addBatch(Batch batch);

	void updateBatch(Batch batch);

	void deleteBatch(int BatchId);

	Batch getBatch(int batchId);

	List<Batch> getAllBatches();

}
