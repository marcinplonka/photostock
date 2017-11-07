package pl.com.bottega.photostock.sales.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Purchase.class)
public abstract class Purchase_ {

	public static volatile SingularAttribute<Purchase, String> number;
	public static volatile SingularAttribute<Purchase, LocalDateTime> purchaseDate;
	public static volatile CollectionAttribute<Purchase, Product> items;
	public static volatile SingularAttribute<Purchase, Client> buyer;

}

