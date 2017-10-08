package ru.leasoft.challenge.aggregator.test.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

class TransactionExecutor {

    @FunctionalInterface
    public interface TransactionProc<E> {
        E transact(Session session);
    }

    private SessionFactory sessionFactory;

    private TransactionExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Factory method used for method chaining
     * @param sessionFactory Hibernate session factory
     * @return Transaction executor
     */
    static TransactionExecutor with(SessionFactory sessionFactory) {
        return new TransactionExecutor(sessionFactory);
    }

    /**
     *
     * @param transactionProc session -> {}, which 'll be executed in transaction bounds
     * @param <E> return value type
     * @return result of TransactionProc execution
     */
    <E> E execute(TransactionProc<E> transactionProc) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        E result = transactionProc.transact(session);
        tx.commit();
        session.close();
        return result;
    }
}
