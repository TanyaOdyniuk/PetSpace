package com.netcracker.service.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface BulletinBoardService {
    int getAllAdPageCount();
    int getMyProfileAdPageCount(BigInteger profileId);

    List<Advertisement> getProfileAds(Integer pageNumber);
    List<Advertisement> getMyProfileAds(BigInteger profileId, Integer pageNumber);

    List<Advertisement> getAllAdAfterCatFilter(Integer pageNumber, Category[] categories);
    List<Advertisement> getAllAdAfterCatFilterFromProfile(Integer pageNumber, Integer profileId, Category[] categories);

    Advertisement addAd(Advertisement ad);

    /**
     *Система должна вывести на экран полную информацию об объявлении, скрыв пустые поля
     */
    List<String> getAd(Advertisement ad);

    /**
     * система должна предоставить пользователю возможность редактировать текущее объявление.
     */
    void updateAd(Advertisement ad);

    /**
     * После получения всех объявлений пользователя (FR: PetSpace.BulletinBoardService.GetMyAds) и просмотра
     * полной информации о конкретном объявлении(FR: PetSpace.BulletinBoardService.GetAd) система должна предоставить
     * пользователю возможность удалить текущее объявление
     */
    void deleteAd(Advertisement ad);

}
