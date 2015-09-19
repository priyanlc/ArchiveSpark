package de.l3s.archivespark.rdd

import de.l3s.archivespark.ResolvedArchiveRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, TaskContext}

/**
 * Created by holzmann on 04.08.2015.
 */
abstract class ResolvedArchiveRDD[Base](parent: RDD[Base]) extends RDD[ResolvedArchiveRecord](parent) {
  protected def record(from: Base): ResolvedArchiveRecord

  override def compute(split: Partition, context: TaskContext): Iterator[ResolvedArchiveRecord] = {
    parent.iterator(split, context).filter(r => r != null).map(r => record(r)).filter(r => r != null)
  }

  override protected def getPartitions: Array[Partition] = parent.partitions
}