package com.ef;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BatchSplitter<T> {
    public List<List<T>> splitToBatches(final List<T> list,
                                        final int batchSize) {
        int noOfBatches = list.size() / batchSize;
        if (list.size() < batchSize) {
            return Collections.singletonList(list);
        }

        List<List<T>> batches = new ArrayList<>(noOfBatches + 1);
        int batchCount = 0;
        while (batchCount < noOfBatches) {
            batches.add(list.subList(batchCount * batchSize, ++batchCount * batchSize));
        }
        batches.add(list.subList(batchCount * batchSize, list.size()));

        return batches;
    }
}
