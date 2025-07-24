package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;

import java.util.List;

public interface ITokenService {

    Token addToken(User user, String token);

    Token refreshToken(String refreshToken, User user) throws Exception;

}
