package com.wheel.app.mvc.servlet;

import com.wheel.app.mvc.annotation.MyController;
import com.wheel.app.mvc.annotation.MyRequestMapping;
import com.wheel.app.mvc.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/9 11:59
 * @Version 1.0
 */
public class MyDispatcherServlet extends HttpServlet {
    //拿到配置文件的属性
    private Properties properties = new Properties();
    //拿到扫描包下的所有文件
    private List<String> classNames = new ArrayList<>();
    //拿到扫描到的类,通过反射机制,实例化,
    // 并且放到ioc容器中(k-v beanName-bean) beanName默认是首字母小写
    private Map<String, Object> ioc = new HashMap<>();
    //url  -  方法
    private Map<String, Method> handlerMapping = new HashMap<>();
    //url  -  controller
    private Map<String, Object> controllerMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        // 2.初始化所有相关联的类,扫描用户设定的包下面所有的类
        doScanner(properties.getProperty("scanPackage"));
        // 3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v beanName-bean) beanName默认是首字母小写
        doInstance();
        // 4.初始化HandlerMapping(将url和method对应上)
        initHandlerMapping();
    }

    /**
     *加载配置文件
     *
     * @param location  文件位置
     */
    private void doLoadConfig(String location) {
        // 把web.xml中的contextConfigLocation对应value值的文件加载到流里面
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(location);
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path);
        // 用Properties文件加载文件里的内容
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *初始化所有相关联的类,扫描用户设定的包下面所有的类
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass()
                .getClassLoader()
                .getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    /**
     *拿到扫描到的类,通过反射机制,实例化,
     * 并且放到ioc容器中(k-v beanName-bean) beanName默认是首字母小写
     */
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            // 把类搞出来,反射来实例化(只有加@MyController需要实例化)
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)) {
                    ioc.put(StringUtils.toLowerFirstWord(clazz.getName()), clazz.newInstance());
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     初始化HandlerMapping(将url和method对应上)
     */
    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        try {
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                if (!clazz.isAnnotationPresent(MyController.class)) {
                    continue;
                }
                //类上请求url
                String baseUrl = "";
                if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                    MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
                    baseUrl = annotation.value();
                }
                Method[] methods = clazz.getDeclaredMethods();
                //方法上请求url
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                        continue;
                    }
                    String url = method.getAnnotation(MyRequestMapping.class).value();

                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    handlerMapping.put(url, method);
                    controllerMap.put(url, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理请求
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理请求逻辑
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (handlerMapping.isEmpty()) {
            return;
        }
        //处理请求上下文
        String url = req.getRequestURI();
        System.out.println("请求中的url参数： " + url);
        String contextPath = req.getContextPath();
        System.out.println("请求中的contextPath上下文参数： " + contextPath);
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        System.out.println("把url中的上下文去掉得到新的url即为我们程序中标识方法的路径： " + url);

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 NOT FOUND!");
            return;
        }
        //根据url请求去HandlerMapping中匹配到对应的Method
        Method method = this.handlerMapping.get(url);

        // 获取方法的参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();

        // 获取请求的参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        // 保存参数值
        Object[] paramValues = new Object[parameterTypes.length];

        // 方法的参数列表
        for (int i = 0; i < parameterTypes.length; i++) {
            // 根据参数名称，做某些处理
            String requestParam = parameterTypes[i].getSimpleName();

            if (requestParam.equals("HttpServletRequest")) {
                // 参数类型已明确，这边强转类型
                paramValues[i] = req;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")) {
                paramValues[i] = resp;
                continue;
            }
            if (requestParam.equals("String")) {
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i] = value;
                }
            }
        }
        // 利用反射机制来调用
        try {
            method.invoke(this.controllerMap.get(url), paramValues);// 第一个参数是method所对应的实例
            // 在ioc容器中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
