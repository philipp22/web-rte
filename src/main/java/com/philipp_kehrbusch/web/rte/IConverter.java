package com.philipp_kehrbusch.web.rte;

public interface IConverter<D extends IDomain, DTO extends IDTO> {

  D fromDTO(DTO dto);

  DTO toDTO(D domain);
}
