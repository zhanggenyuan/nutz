package org.nutz.dao.util.cri;

import org.nutz.dao.Condition;
import org.nutz.dao.DaoException;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.GroupBy;
import org.nutz.lang.Lang;

public class GroupBySet extends OrderBySet implements GroupBy {

    private static final long serialVersionUID = 1L;

	private String[] names;
	
	private Condition having;
	
	public GroupBySet(String...names) {
		if (Lang.length(names) == 0)
			throw new DaoException("NULL for GroupBy");
		this.names = names;
	}
	
	public GroupBy having(Condition cnd) {
		having = cnd;
		return this;
	}
	
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(" GROUP BY ");
		for (String name : names) {
			sb.append(_fmtcolnm(en, name));
			sb.append(",");
		}
		sb.setCharAt(sb.length() - 1, ' ');
		if (having != null) {
			sb.append("HAVING ");
			if (having instanceof SqlExpressionGroup) {
				((SqlExpressionGroup)having).setTop(false);
				sb.append(having.toSql(en));
			} else {
				String sql = having.toSql(en).trim();
				if (sql.length() > 5 && "WHERE".equalsIgnoreCase(sql.substring(0,  5)))
					sql = sql.substring(5).trim();
				sb.append(sql);
			}
		}
	}
	
	public GroupBy groupBy(String ... names) {
	    this.names = names;
	    return this;
	}
}
