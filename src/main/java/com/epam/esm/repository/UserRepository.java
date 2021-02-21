package com.epam.esm.repository;

import com.epam.esm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides with ability to
 * transfer {@code User} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByLogin(String login);
}
