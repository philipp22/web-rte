package com.philipp_kehrbusch.web.rte;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractRestController<
        D extends IDomain<D>,
        DTO extends IDTO,
        DAO extends JpaRepository<D, Long>,
        CONV extends IConverter<D, DTO>> {

  private static int MAX_GET_COUNT = 50;
  private static int DEFAULT_GET_COUNT = 10;

  private DAO dao;
  private CONV converter;

  public AbstractRestController(DAO dao, CONV converter) {
    this.dao = dao;
    this.converter = converter;
  }

  public DTO get(long id) {
    var res = dao.findById(id);
    return res.map(d -> converter.toDTO(d)).orElse(null);
  }

  public List<DTO> getAll(Integer pageWrapper, Integer countWrapper) {
    var page = pageWrapper != null ? pageWrapper : 0;
    var count = countWrapper != null && countWrapper <= MAX_GET_COUNT ? countWrapper : DEFAULT_GET_COUNT;
    var pageRequest = PageRequest.of(page, count);
    var res = dao.findAll(pageRequest);
    return res.stream()
            .map(converter::toDTO)
            .collect(Collectors.toList());
  }

  public DTO create(DTO dto) {
    var domain = converter.fromDTO(dto);
    if (domain != null) {
      var res = dao.save(domain);
      return converter.toDTO(res);
    } else {
      return null;
    }
  }

  public DTO update(DTO dto) {
    if (dto == null) {
      return null;
    }
    var domain = dao.findById(dto.getId());
    if (domain.isPresent()) {
      var other = converter.fromDTO(dto);
      domain.get().merge(other);
      return converter.toDTO(domain.get());
    } else {
      return null;
    }
  }

  public void delete(long id) {
    dao.deleteById(id);
  }
}
