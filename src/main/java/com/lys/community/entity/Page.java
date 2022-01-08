package com.lys.community.entity;

/**
 * 封装分页
 */
public class Page {
    //当前页码
    private int currentPage = 1;
//   显示上限
    private int limit=5;
//    数据总数
    private int rows;
//    查询路径
    private String path;

    public Page(){}

    public Page(int currentPage, int limit, int rows, String path) {
        this.currentPage = currentPage;
        this.limit = limit;
        this.rows = rows;
        this.path = path;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage >= 1) {
            this.currentPage = currentPage;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Page{" +
                "currentPage=" + currentPage +
                ", limit=" + limit +
                ", rows=" + rows +
                ", path='" + path + '\'' +
                '}';
    }

//    获取当前页的起始行
    public int getOffset() {
        return (currentPage - 1) * limit;
    }

//    获取总页数
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        }else {
        //    多出来不够完整一页给多算一页
            return rows / limit + 1;
        }
    }
//    获取起始页码
    public int getForm() {
        int form = currentPage - 2;
        return form < 1 ? 1 : form;
    }
//    获取结束页码
    public int getTo() {
        int to = currentPage + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
