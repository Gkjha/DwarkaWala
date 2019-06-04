package Fragments;

import java.util.List;

import Models.Banners;

public interface FirebaseLoadDone {

    void OnFirebaseSuccess(List<Banners> bannersList);
    void OnFirebaseFiled(String message);
}
