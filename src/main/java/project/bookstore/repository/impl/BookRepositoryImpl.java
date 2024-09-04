package project.bookstore.repository.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import project.bookstore.model.Book;
import project.bookstore.repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cannot save book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = criteriaQuery.from(Book.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Cannot find all books", e);
        }
    }
}
