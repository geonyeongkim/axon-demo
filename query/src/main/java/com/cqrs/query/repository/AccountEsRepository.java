package com.cqrs.query.repository;

import com.cqrs.query.model.document.AccountEsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface AccountEsRepository extends ElasticsearchRepository<AccountEsDocument, String> {

}
