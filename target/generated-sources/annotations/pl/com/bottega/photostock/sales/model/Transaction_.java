package pl.com.bottega.photostock.sales.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static volatile SingularAttribute<Transaction, LocalDateTime> dateTime;
	public static volatile SingularAttribute<Transaction, Long> clientId;
	public static volatile SingularAttribute<Transaction, Long> cents;
	public static volatile SingularAttribute<Transaction, String> description;
	public static volatile SingularAttribute<Transaction, String> currency;

}

