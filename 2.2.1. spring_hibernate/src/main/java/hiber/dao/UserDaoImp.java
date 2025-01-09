package hiber.dao;


import hiber.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   private final SessionFactory sessionFactory;

   @Autowired
   public UserDaoImp(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Transactional(readOnly = true, rollbackFor = HibernateException.class)
   @Override
   public void getUserByCarModelAndSeries(String model, int series) {
      try {
         Session session = sessionFactory.getCurrentSession();
         String hql = "SELECT u FROM User u WHERE u.id = (SELECT c.user.id FROM Car c WHERE c.model = :carModel AND c.series = :carSeries)";
         List<User> user = session.createQuery(hql, User.class)
                 .setParameter("carModel", model)
                 .setParameter("carSeries", series).list();
         System.out.println(user.toString());
      } catch (HibernateException e) {
         throw new RuntimeException(e);
      }
   }
}
