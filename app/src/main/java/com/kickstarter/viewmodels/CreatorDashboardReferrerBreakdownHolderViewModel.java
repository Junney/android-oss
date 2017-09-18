package com.kickstarter.viewmodels;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.kickstarter.libs.ActivityViewModel;
import com.kickstarter.libs.Environment;
import com.kickstarter.libs.ReferrerType;
import com.kickstarter.libs.utils.NumberUtils;
import com.kickstarter.libs.utils.PairUtils;
import com.kickstarter.models.Project;
import com.kickstarter.services.apiresponses.ProjectStatsEnvelope;
import com.kickstarter.ui.viewholders.CreatorDashboardReferrerBreakDownViewHolder;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public interface CreatorDashboardReferrerBreakdownHolderViewModel {

  interface Inputs {
    void projectAndReferrerStatsInput(Pair<Project, List<ProjectStatsEnvelope.ReferrerStats>> projectAndReferrerStats);
  }

  interface Outputs {
    Observable<Double> customReferrerPercent();
    Observable<String> customReferrerPercentText();
    Observable<Integer> customReferrerPledgedAmount();
    Observable<Double> externalReferrerPercent();
    Observable<String> externalReferrerPercentText();
    Observable<Integer> externalReferrerPledgedAmount();
    Observable<Double> internalReferrerPercent();
    Observable<String> internalReferrerPercentText();
    Observable<Integer> internalReferrerPledgedAmount();
    Observable<Double> unknownReferrerPercent();
    Observable<Integer> unknownReferrerPledgedAmount();
    Observable<Pair<Project, Integer>> projectAndCustomReferrerPledgedAmount();
    Observable<Pair<Project, Integer>> projectAndExternalReferrerPledgedAmount();
    Observable<Pair<Project, Integer>> projectAndInternalReferrerPledgedAmount();
  }

  final class ViewModel extends ActivityViewModel<CreatorDashboardReferrerBreakDownViewHolder> implements Inputs, Outputs {

    public ViewModel(final @NonNull Environment environment) {
      super(environment);

      final Observable<Project> currentProject = this.projectAndReferrerStatsInput
        .map(PairUtils::first);

      final Observable<List<ProjectStatsEnvelope.ReferrerStats>> referrerStats = this.projectAndReferrerStatsInput
        .map(PairUtils::second);

      final Observable<List<ProjectStatsEnvelope.ReferrerStats>> internalReferrers = referrerStats
        .flatMap(rs ->
          Observable.from(rs).filter(r -> r.referrerType() == ReferrerType.INTERNAL).toList()
        );

      final Observable<List<ProjectStatsEnvelope.ReferrerStats>> externalReferrers = referrerStats
        .flatMap(rs ->
          Observable.from(rs).filter(r -> r.referrerType() == ReferrerType.EXTERNAL).toList()
        );

      final Observable<List<ProjectStatsEnvelope.ReferrerStats>> customReferrers = referrerStats
        .flatMap(rs ->
          Observable.from(rs).filter(r -> r.referrerType() == ReferrerType.CUSTOM).toList()
        );

      final Observable<List<ProjectStatsEnvelope.ReferrerStats>> unknownReferrers = referrerStats
        .flatMap(rs ->
          Observable.from(rs).filter(r -> r.referrerType() == null).toList()
        );

      this.customReferrerPercent = customReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0d, (accum, stat) -> accum + stat.percentageOfDollars())
        );

      this.customReferrerPercentText = this.customReferrerPercent
        .map(percent -> NumberUtils.flooredPercentage((percent.floatValue() * 100f)));

      this.customReferrerPledgedAmount = customReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0, (accum, stat) -> accum + stat.pledged())
        );

      this.projectAndCustomReferrerPledgedAmount = Observable.combineLatest(
        currentProject,
        this.customReferrerPledgedAmount,
        Pair::create
      );

      this.externalReferrerPercent = externalReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0d, (accum, stat) -> accum + stat.percentageOfDollars())
        );

      this.externalReferrerPercentText = externalReferrerPercent
        .map(percent -> NumberUtils.flooredPercentage((percent.floatValue() * 100f)));

      this.externalReferrerPledgedAmount = customReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0, (accum, stat) -> accum + stat.pledged())
        );

      this.projectAndExternalReferrerPledgedAmount = Observable.combineLatest(
        currentProject,
        this.externalReferrerPledgedAmount,
        Pair::create
      );

      this.internalReferrerPercent = internalReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0d, (accum, stat) -> accum + stat.percentageOfDollars())
        );

      this.internalReferrerPercentText = internalReferrerPercent
        .map(percent -> NumberUtils.flooredPercentage((percent.floatValue() * 100f)));

      this.internalReferrerPledgedAmount = customReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0, (accum, stat) -> accum + stat.pledged())
        );

      this.projectAndInternalReferrerPledgedAmount = Observable.combineLatest(
        currentProject,
        this.internalReferrerPledgedAmount,
        Pair::create
      );

      this.unknownReferrerPercent = unknownReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0d, (accum, stat) -> accum + stat.percentageOfDollars())
        );

      this.unknownReferrerPledgedAmount = customReferrers
        .flatMap(rs ->
          Observable.from(rs)
            .reduce(0, (accum, stat) -> accum + stat.pledged())
        );
    }

    public final Inputs inputs = this;
    public final Outputs outputs = this;

    private final PublishSubject <Pair<Project, List<ProjectStatsEnvelope.ReferrerStats>>> projectAndReferrerStatsInput = PublishSubject.create();

    private final Observable<Double> customReferrerPercent;
    private final Observable<String> customReferrerPercentText;
    private final Observable<Integer> customReferrerPledgedAmount;
    private final Observable<Double> externalReferrerPercent;
    private final Observable<String> externalReferrerPercentText;
    private final Observable<Integer> externalReferrerPledgedAmount;
    private final Observable<Double> internalReferrerPercent;
    private final Observable<String> internalReferrerPercentText;
    private final Observable<Integer> internalReferrerPledgedAmount;
    private final Observable<Pair<Project, Integer>> projectAndCustomReferrerPledgedAmount;
    private final Observable<Pair<Project, Integer>> projectAndExternalReferrerPledgedAmount;
    private final Observable<Pair<Project, Integer>> projectAndInternalReferrerPledgedAmount;


    private final Observable<Double> unknownReferrerPercent;
    private final Observable<Integer> unknownReferrerPledgedAmount;


    @Override
    public void projectAndReferrerStatsInput(final @NonNull Pair<Project, List<ProjectStatsEnvelope.ReferrerStats>> projectAndReferrerStats) {
      this.projectAndReferrerStatsInput.onNext(projectAndReferrerStats);
    }

    @Override
    public @NonNull Observable<Pair<Project, Integer>> projectAndCustomReferrerPledgedAmount() {
      return this.projectAndCustomReferrerPledgedAmount;
    }
    @Override
    public @NonNull Observable<Pair<Project, Integer>> projectAndExternalReferrerPledgedAmount() {
      return this.projectAndExternalReferrerPledgedAmount;
    }
    @Override
    public @NonNull Observable<Pair<Project, Integer>> projectAndInternalReferrerPledgedAmount() {
      return this.projectAndInternalReferrerPledgedAmount;
    }
    @Override
    public @NonNull Observable<Double> customReferrerPercent() {
      return this.customReferrerPercent;
    }
    @Override
    public @NonNull Observable<String> customReferrerPercentText() {
      return this.customReferrerPercentText;
    }
    @Override
    public @NonNull Observable<Integer> customReferrerPledgedAmount() {
      return this.customReferrerPledgedAmount;
    }
    @Override
    public @NonNull Observable<Double> externalReferrerPercent() {
      return this.externalReferrerPercent;
    }
    @Override
    public @NonNull Observable<String> externalReferrerPercentText() {
      return this.externalReferrerPercentText;
    }
    @Override
    public @NonNull Observable<Integer> externalReferrerPledgedAmount() {
      return this.externalReferrerPledgedAmount;
    }
    @Override
    public @NonNull Observable<Double> internalReferrerPercent() {
      return this.internalReferrerPercent;
    }
    @Override
    public @NonNull Observable<String> internalReferrerPercentText() {
      return this.internalReferrerPercentText;
    }
    @Override
    public @NonNull Observable<Integer> internalReferrerPledgedAmount() {
      return this.internalReferrerPledgedAmount;
    }
    @Override
    public @NonNull Observable<Double> unknownReferrerPercent() {
      return this.unknownReferrerPercent;
    }
    @Override
    public @NonNull Observable<Integer> unknownReferrerPledgedAmount() {
      return this.unknownReferrerPledgedAmount;
    }
  }
}
