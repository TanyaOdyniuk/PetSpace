package com.netcracker.service.charts;

import com.netcracker.model.user.Profile;

import java.util.List;

public interface ChartService {
    List<Profile> calculateCharts();

    List<Profile> topCharts();

    void refreshChart();

    void winnerPaid();

    Profile goToParticipantPage(Profile profile);

    List<Profile> topThree();
}
