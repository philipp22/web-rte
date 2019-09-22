package com.philipp_kehrbusch.web.rte;

public interface IDomain<D extends IDomain> {
  long getId();

  void merge(D other);
}
