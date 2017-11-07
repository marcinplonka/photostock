package pl.com.bottega.photostock.sales.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LightBox.class)
public abstract class LightBox_ {

	public static volatile SingularAttribute<LightBox, String> number;
	public static volatile SingularAttribute<LightBox, String> name;
	public static volatile ListAttribute<LightBox, Picture> items;

}

