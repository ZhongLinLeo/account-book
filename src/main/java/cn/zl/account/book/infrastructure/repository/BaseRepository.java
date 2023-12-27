package cn.zl.account.book.infrastructure.repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 注入 entityManager
 *
 * @author lin.zl
 */
public class BaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

}
