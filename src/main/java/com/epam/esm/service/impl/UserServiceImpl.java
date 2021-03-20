package com.epam.esm.service.impl;

import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.AuditedOrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.OrderSearchCriteria;
import com.epam.esm.service.search.criteria.UserSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import com.epam.esm.service.util.PaginationUtil;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.service.util.UserValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LogManager.getLogger(UserServiceImpl.class);

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuditedOrderService auditedOrderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator,
                           PaginationValidator paginationValidator, PasswordEncoder passwordEncoder,
                           AuditedOrderService auditedOrderService) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
        this.passwordEncoder = passwordEncoder;
        this.auditedOrderService = auditedOrderService;
    }

    @Override
    public User getUserByLogin(String login) throws ServiceException {
        userValidator.validateLogin(login);
        try {
            return userRepository.getUserByLogin(login);
        } catch (NoResultException e) {
            log.error("Following exception was thrown in getUser(String login): " + e.getMessage());
            throw new ServiceException(
                    "Failed to get user by it login: " + login, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER
            );
        }
    }

    @Override
    public User getUserById(int userId) throws ServiceException {
        userValidator.validateId(userId);
        try {
            var user = userRepository.findById(userId).orElseThrow(() -> {
                log.error("Failed to get user by it id: " + userId);
                return new ServiceException("Failed to get user by it id: " + userId,
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_USER
                );
            });
            setAuditedOrdersToUser(user);

            return user;
        } catch (DataAccessException e) {
            log.error("Following exception was thrown in getUser(int id): " + e.getMessage());
            throw new ServiceException("Failed to get user by it id: " + userId, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria searchCriteria, int page, int size,
                                        SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(page, size);

        if (searchCriteria == null) {
            searchCriteria = UserSearchCriteria.getDefaultUserRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        userValidator.validateUserSearchCriteria(searchCriteria);

        try {
            var users = userRepository.findAll(
                    PageRequest.of(--page, size, searchCriteria.getSort())
            ).getContent();
            users.forEach(this::setAuditedOrdersToUser);

            return users;
        } catch (DataAccessException e) {
            log.error("Following exception was thrown in getAllUsersByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get users", ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    private void setAuditedOrdersToUser(User user) {
        var orderSearchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();
        try {
            user.setOrders(
                    new HashSet<>(
                            auditedOrderService.getAuditedOrdersByUserId(
                                    user.getId(), orderSearchCriteria, DEFAULT_PAGE, DEFAULT_SIZE,
                                    orderSearchCriteria.getSortType(), orderSearchCriteria.getSortBy()
                            )
                    )
            );
        } catch (ServiceException e) {
            log.error("Failed to get audited order by user id: " + user.getId());
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        paginationValidator.validateSize(size);
        try {
            return PaginationUtil.getLastPage((int) userRepository.count(), size);
        } catch (DataAccessException | PersistenceException e) {
            log.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public User addUser(User user) throws ServiceException {
        userValidator.validateUser(user);
        try {
            user.setRole(Role.getUserRole());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            log.error("Following exception was thrown in addUser(): " + e.getMessage());
            throw new ServiceException("Failed to add user", ErrorCodeEnum.FAILED_TO_ADD_USER);
        }
    }
}
