package sce.finalprojects.sceprojectbackend.factories;

import sce.finalprojects.sceprojectbackend.datatypes.ArticleDO;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;
import sce.finalprojects.sceprojectbackend.managers.CacheManager.ObjectType;

public class ArticlesFactory extends BaseFactory<ArticleDO> {

	@Override
	protected ArticleDO handle(String id) {
		return new ArticleDO(id);
	}

	@Override
	protected ObjectType getType() {
		return CacheManager.ObjectType.ARTICLE;
	}

}
