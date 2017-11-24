package com.netcracker.service.bulletinBoard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.model.user.Profile;
import java.util.List;

public interface BulletinBoard {

//    отображать список объявлений других пользователей постранично с возможностью
//    выбора количества объявлений на  странице, отсортировав их в установленном порядке
    List<Advertisement> getAds();

//    отображать список объявлений текущего пользователя постранично с возможностью
//    выбора количества объявлений на  странице, отсортировав их в установленном порядке
    List<Advertisement> getMyAds(Profile profile);

//    Система должна позволять отсортировать список объявлений по следующим правилам:
//            1. Объявления в статусе VIP всегда находятся выше остальных.
    List<Advertisement> sortAd(List<Advertisement> listAds);

//    Система должна позволять пользователю возможность добавить новое объявление с одной категорией
    void newAd(Advertisement ad, Category categoryAd);

//    Система должна вывести на экран полную информацию об объявлении, скрыв пустые поля
    List<String> getAd(Advertisement ad);

//    После получения всех объявлений пользователя (FR: PetSpace.BulletinBoard.GetMyAds) и просмотра полной информации о конкретном объявлении(FR: PetSpace.BulletinBoard.GetAd)
//    система должна предоставить пользователю возможность редактировать текущее объявление.
    void updateAd(Advertisement ad);

//    После получения всех объявлений пользователя (FR: PetSpace.BulletinBoard.GetMyAds) и просмотра
//    полной информации о конкретном объявлении(FR: PetSpace.BulletinBoard.GetAd) система должна предоставить
//    пользователю возможность удалить текущее объявление
    void deleteAd(Advertisement ad);

//    Система должна позволять пользователю выбрать одну категорию для своего объявления
    void chooseCategory(List<Advertisement> listAds, Category categoryAd);

//    После получения списка всех объявлений (FR: PetSpace.BulletinBoard.GetAds) или объявлений текущего пользователя
//    (FR: PetSpace.BulletinBoard.GetMyAds) cистема должна позволять фильтровать список
//    объявлений по выбранной категории/категориям
    void filterByCategory(List<Advertisement> listAds, List<Category> categoryAds);

}