package com.mile.nokenzivot.mail_handler;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
class CustomPersistenceService<T> {

  @PersistenceContext
  EntityManager em;

  void persist(T entity) {
    try (Session session = em.unwrap(Session.class)) {
      session.persist(entity);
    }
  }

  void update(T entity) {
    try (Session session = em.unwrap(Session.class)) {
      session.update(entity);
    }
  }
}
