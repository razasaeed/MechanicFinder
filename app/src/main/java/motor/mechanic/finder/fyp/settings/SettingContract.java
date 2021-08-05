package motor.mechanic.finder.fyp.settings;

import motor.mechanic.finder.fyp.BasePresenter;
import motor.mechanic.finder.fyp.BaseView;
import motor.mechanic.finder.fyp.DataModels.UserDataModel;

/**
 * Created by hieuapp on 10/12/2017.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter> {

        void showProfile(UserDataModel profile);

    }

    interface Presenter extends BasePresenter {
        UserDataModel getUserProfile();

        void refreshUserProfile();

        void logout();

        void deleteUserProfile();
    }
}
