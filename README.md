# Community
笔记1：
拦截器和过滤器，filter基于Servlet容器，而拦截器是独立存在，在任何情况下都可以使用。
Filter的执行由Servlet容器回调完成，而拦截器通常通过动态代理的方式来完成,Filter声明周期由
