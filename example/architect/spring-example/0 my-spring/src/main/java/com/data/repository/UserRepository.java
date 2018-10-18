package com.data.repository;

import com.data.domain.User;
import com.data.domain.User;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 自动生成的Bean名称是userRepository， 很多也直接 extends CrudRepository<l_1_User, Long>
 * @RepositoryDefinition 可以替代extends
 *
 * 手册: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 * API： https://docs.spring.io/spring-data/data-jpa/docs/current/api/
 * JPA： http://www.objectdb.com/api/java/jpa/annotations
 * hibernate: http://docs.jboss.org/hibernate/jpa/2.1/api/
 *
 * 初始化mysql数据库：
 * CREATE DATABASE `db_example` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 查找
     *
     * 返回值可以是：n_n_User、Optional<n_n_User>、Collection<n_n_User>
     */
    Optional<User> findById(Long id);
    List<User>  findByNameOrderByEmailAsc(String name);
    List<User>  findByNameOrEmail(String name, String email);

    /**
     * 返回值可以写 List<n_n_User>、ArrayList<n_n_User> 等
     *
     * 这里不能写user，报异常：result returns more than one elements ？？？？
     */
    ArrayList<User> findDistinctUserByName(String name);

    /**
     * 修改表的设计
     */

//    /**
//     * 修改
//     */
//    @Modifying
//    @Query("update user_main set name = ?1 where id = ?2")
//    int modifyByIdAndUserId(String name, Integer id);
//
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * page、sort
     */

    /**
     * pageable
     * 1. 不带sort: PageRequest(0, 3)
     * 2. 自动构造sort：PageRequest(0, 3, Sort.Direction.DESC,"id")
     * 3. 传入一个sort：new Sort(Sort.Direction.DESC,"id")，每个属性也可以使用不同的排序方式
     *
     * 返回值：
     * 1. Page<l_1_User>  中包含查询的总条数，可能导致较大查询开销
     * 2. Slice<n_n_User> 中不需要包含总条数，开销会小很多； Slice<n_n_User>   findAll(Pageable pageable)
     * 3. List<n_n_User>  只返回一次                        List<n_n_User>   findAll(Pageable pageable);
     */
    Page<User>  findAll(Pageable pageable);

    /**
     * 使用 nativeQuery ，就不能使用Pageable
     *
     * 解决方法：
     *      https://stackoverflow.com/questions/38349930/spring-data-and-native-query-with-pagination
     * 1. 添加 countQuery 语句, 用来给Page返回总个数，填充 Page<n_n_User> 的内容；如果返回值是 Slice<n_n_User>, 则不需要countQuery
     * 2. 在query语句最后，使用 "\n-- #pageable\n"
     * 3. 如果要排序
     *      在query语句中添加 ORDER BY id DESC, 或者仍然在 pageable 指定排序
     *      但是不能同时指定这两个
     */
    @Query(value = "select * from user_table ORDER BY id ASC \n-- #pageable\n",
            countQuery = "select count(*) from user_table",
            nativeQuery = true)
    Page<User>  findAllUser(Pageable pageable);

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

//    Page<l_1_User>  findFirst10ByName(String name, Pageable pageable);

    /**
     * 事务
     */
//    @Transactional(timeout = 10)
//    @Query("select * from user u where u.name = ?1")
//    l_1_User findByName(String name);
//
//    /**
//     * 查询
//     */
//    @Query("select u from l_1_User u where u.name = ?1")
//    l_1_User findByName(String name);

        /*
    Page<n_n_User> users = repository.findAll(new PageRequest(1, 20));
    List<n_n_User> removeByLastname(String lastname);

    返回空值的处理
    是否使用option

            有多个值，但是只返回uyser的时候？
    Page<n_n_User> findByLastname(String lastname, Pageable pageable);

    Slice<n_n_User> findByLastname(String lastname, Pageable pageable);

    List<n_n_User> findByLastname(String lastname, Sort sort);

    List<n_n_User> findByLastname(String lastname, Pageable pageable);

    'K' smallest
            @EnableJpaRepositories
    */

    /**
     * 参数转换
     */
    //(“select p from Person p where p.id=:id”);

    @Query(value = "select u.name from user_table u join department d on u.department = d.name", nativeQuery = true)
    List<String>  findUserJoinDepartment();
}
