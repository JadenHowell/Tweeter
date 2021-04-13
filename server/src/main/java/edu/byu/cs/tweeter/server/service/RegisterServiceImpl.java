package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.RegisterService;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterServiceImpl implements RegisterService {
    private String bucketName = "tweeter-prof-photos";
    private String baseURL = "https://tweeter-prof-photos.s3-us-west-2.amazonaws.com/";
    private String defaultPhoto = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (!getUserDAO().getUser(request.getUsername()).getMessage().contains("not found")) {
            return new RegisterResponse(true, "Username already exists, pick a new one or login.");
        }
        uploadPhoto(request);

        getUserDAO().putUser(request);

        return new RegisterResponse(new User(request.getFirstName(),request.getLastName(),
                request.getUsername(), request.getPhotoURL()), getAuthTokenDAO().newAuthToken(request.getUsername()));
    }

    UserDAO getUserDAO() { return new UserDAO(); }
    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }

    private void uploadPhoto(RegisterRequest request){
        if(request.getStringPhoto() == null || request.getStringPhoto().length() == 0){
            request.setPhotoURL(defaultPhoto);
            return;
        }
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();
        String s = request.getStringPhoto();
        ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(s));
        UUID uuid = UUID.randomUUID();
        s3.putObject(bucketName, uuid.toString(), stream, null);
        String url = baseURL + uuid.toString();
        request.setPhotoURL(url);
    }
}
