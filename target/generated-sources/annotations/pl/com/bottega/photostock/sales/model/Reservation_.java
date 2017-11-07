package pl.com.bottega.photostock.sales.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Reservation.class)
public abstract class Reservation_ {

	public static volatile SingularAttribute<Reservation, Client> owner;
	public static volatile SingularAttribute<Reservation, String> number;
	public static volatile CollectionAttribute<Reservation, Product> items;

}

