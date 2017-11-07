package pl.com.bottega.photostock.sales.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractProduct.class)
public abstract class AbstractProduct_ {

	public static volatile SingularAttribute<AbstractProduct, Long> number;
	public static volatile SingularAttribute<AbstractProduct, String> priceCurrency;
	public static volatile SingularAttribute<AbstractProduct, Client> reservedBy;
	public static volatile SingularAttribute<AbstractProduct, Boolean> active;
	public static volatile SingularAttribute<AbstractProduct, Long> priceAmount;
	public static volatile SingularAttribute<AbstractProduct, Client> ownerId;

}

