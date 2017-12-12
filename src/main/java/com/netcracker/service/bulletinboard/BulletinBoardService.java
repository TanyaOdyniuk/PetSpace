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
    //    отображать список объявлений других пользователей постранично с возможностью
//    выбора количества объявлений на  странице, отсортировав их в установленном порядке
    List<Advertisement> getProfileAds(Integer pageNumber);

    //    отображать список объявлений текущего пользователя постранично с возможностью
//    выбора количества объявлений на  странице, отсортировав их в установленном порядке
    List<Advertisement> getMyProfileAds(BigInteger profileId, Integer pageNumber);


    //    Система должна позволять отсортировать список объявлений по следующим правилам:
//            1. Объявления в статусе VIP всегда находятся выше остальных.
    List<Advertisement> sortAds(List<Advertisement> listAds);

    //    Система должна позволять пользователю возможность добавить новое объявление с одной категорией
    void newAd(Advertisement ad);

    //    Система должна вывести на экран полную информацию об объявлении, скрыв пустые поля
    List<String> getAd(Advertisement ad);

    //    система должна предоставить пользователю возможность редактировать текущее объявление.
    void updateAd(Advertisement ad);

    //    После получения всех объявлений пользователя (FR: PetSpace.BulletinBoardService.GetMyAds) и просмотра
//    полной информации о конкретном объявлении(FR: PetSpace.BulletinBoardService.GetAd) система должна предоставить
//    пользователю возможность удалить текущее объявление
    void deleteAd(Advertisement ad);

    //    Система должна позволять пользователю выбрать одну категорию для своего объявления
    void chooseCategoryForAd(Advertisement listAds, Category categoryAd);

    //    cистема должна позволять фильтровать список объявлений по выбранной категории/категориям
    void filterAdsByCategory(List<Advertisement> listAds, List<Category> categoryAds);

}
