package pl.com.bottega.photostock.sales.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Client.class)
public abstract class Client_ {

	public static volatile SingularAttribute<Client, String> number;
	public static volatile SingularAttribute<Client, Address> address;
	public static volatile SingularAttribute<Client, String> name;
	public static volatile ListAttribute<Client, Transaction> transactions;
	public static volatile SingularAttribute<Client, ClientStatus> status;

}

