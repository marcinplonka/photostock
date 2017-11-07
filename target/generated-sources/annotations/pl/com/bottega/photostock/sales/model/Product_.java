package pl.com.bottega.photostock.sales.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Long> number;
	public static volatile SingularAttribute<Product, String> priceCurrency;
	public static volatile SingularAttribute<Product, Client> reservedBy;
	public static volatile SingularAttribute<Product, Boolean> active;
	public static volatile SingularAttribute<Product, Long> priceAmount;
	public static volatile SingularAttribute<Product, Client> ownerId;

}

