package info.archinnov.achilles.entity.operations;

import info.archinnov.achilles.entity.context.AchillesPersistenceContext;
import info.archinnov.achilles.proxy.interceptor.AchillesJpaEntityInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityRefresher
 * 
 * @author DuyHai DOAN
 * 
 */
public class AchillesEntityRefresher
{
	private static final Logger log = LoggerFactory.getLogger(AchillesEntityRefresher.class);

	private AchillesEntityProxifier proxifier;
	private AchillesEntityLoader loader;

	public AchillesEntityRefresher() {}

	public AchillesEntityRefresher(AchillesEntityLoader loader, AchillesEntityProxifier proxifier) {
		this.loader = loader;
		this.proxifier = proxifier;
	}

	public <T> void refresh(AchillesPersistenceContext context)
	{
		log.debug("Refreshing entity of class {} and primary key {}", context.getEntityClass()
				.getCanonicalName(), context.getPrimaryKey());

		Object entity = context.getEntity();

		AchillesJpaEntityInterceptor<Object> interceptor = proxifier.getInterceptor(entity);

		Object freshEntity = loader.load(context, context.getEntityClass());

		interceptor.getDirtyMap().clear();
		interceptor.getLazyAlreadyLoaded().clear();
		interceptor.setTarget(freshEntity);
	}
}