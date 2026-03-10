package admin.dao;

import admin.model.Category;
import config.DbConfig;

import java.util.List;

public class CategoryDao extends DbConfig {

    public List<Category> findAll() {
        return get().withHandle(h ->
                h.createQuery("""
                        SELECT id, name
                        FROM categories
                        ORDER BY name
                        """)
                        .mapToBean(Category.class)
                        .list()
        );
    }
}
