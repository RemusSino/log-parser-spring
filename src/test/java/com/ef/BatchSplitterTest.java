package com.ef;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BatchSplitterTest {
    private BatchSplitter batchSplitter = new BatchSplitter();

    @Test
    @SuppressWarnings("unchecked")
    public void testBatchSplit() {
        List<Integer> ints = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        List<List<Integer>> batchList = batchSplitter.splitToBatches(ints, 3);
        Assertions.assertThat(batchList).hasSize(7);
        Assertions.assertThat(batchList.stream().flatMap(List::stream).collect
                (Collectors.toList())).containsAll(ints);
    }
}
