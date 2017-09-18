package com.kickstarter.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kickstarter.R;
import com.kickstarter.libs.KSCurrency;
import com.kickstarter.models.Project;
import com.kickstarter.services.apiresponses.ProjectStatsEnvelope;
import com.kickstarter.ui.views.ReferrerBreakdownView;
import com.kickstarter.viewmodels.CreatorDashboardReferrerBreakdownHolderViewModel;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.kickstarter.libs.rx.transformers.Transformers.observeForUI;
import static com.kickstarter.libs.utils.ObjectUtils.requireNonNull;

public class CreatorDashboardReferrerBreakDownViewHolder extends KSViewHolder {
  private final CreatorDashboardReferrerBreakdownHolderViewModel.ViewModel viewModel;
  private final ReferrerBreakdownView referrerBreakdownView;
  protected @Bind(R.id.referrer_breakdown_chart_layout) LinearLayout referrerBreakdownLayout;
  protected @Bind(R.id.amount_pledged_via_kickstarter_text_view) TextView amountPledgedViaInternalTextView;
  protected @Bind(R.id.amount_pledged_via_external_text_view) TextView amountPledgedViaExternalTextView;
  protected @Bind(R.id.amount_pledged_via_custom_text_view) TextView amountPledgedViaCustomTextView;
  protected @Bind(R.id.percent_via_custom_text_view) TextView percentCustomTextView;
  protected @Bind(R.id.percent_via_external_text_view) TextView percentExternalTextView;
  protected @Bind(R.id.percent_via_kickstarter_text_view) TextView percentInternalTextView;

  private KSCurrency ksCurrency;

  public CreatorDashboardReferrerBreakDownViewHolder(final @NonNull View view) {
    super(view);
    ButterKnife.bind(this, view);
    this.ksCurrency = this.environment().ksCurrency();
    this.referrerBreakdownView = new ReferrerBreakdownView(context());
    this.referrerBreakdownLayout.addView(referrerBreakdownView, 0);
    this.referrerBreakdownView.invalidate(0,0,view.getWidth(), view.getHeight());
    this.referrerBreakdownView.invalidate();

    this.viewModel = new CreatorDashboardReferrerBreakdownHolderViewModel.ViewModel(environment());

    this.viewModel.outputs.customReferrerPercent()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(percent -> this.referrerBreakdownView.setCustomAngleAndColor(percent * 360d));

    this.viewModel.outputs.customReferrerPercentText()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(this.percentCustomTextView::setText);

    this.viewModel.outputs.customReferrerPledgedAmount()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(amount -> this.amountPledgedViaCustomTextView.setText(String.valueOf(amount)));

    this.viewModel.outputs.externalReferrerPercent()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(percent -> this.referrerBreakdownView.setExternalAngleAndColor(percent * 360d));

    this.viewModel.outputs.externalReferrerPercentText()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(this.percentExternalTextView::setText);

    this.viewModel.outputs.internalReferrerPercent()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(percent -> this.referrerBreakdownView.setInternalAngleAndColor(percent * 360d));

    this.viewModel.outputs.internalReferrerPercent()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(percent -> this.percentInternalTextView.setText(String.valueOf(percent * 100d)));

    this.viewModel.outputs.internalReferrerPercentText()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(this.percentInternalTextView::setText);

    this.viewModel.outputs.projectAndCustomReferrerPledgedAmount()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(pa -> this.setAmountPledgedTextViewText(pa, this.amountPledgedViaCustomTextView));

    this.viewModel.outputs.projectAndExternalReferrerPledgedAmount()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(pa -> this.setAmountPledgedTextViewText(pa, this.amountPledgedViaExternalTextView));

    this.viewModel.outputs.projectAndInternalReferrerPledgedAmount()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(pa -> this.setAmountPledgedTextViewText(pa, this.amountPledgedViaInternalTextView));

    this.viewModel.outputs.unknownReferrerPercent()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(percent -> this.referrerBreakdownView.setUnknownAngleAndColor(percent * 360d));
  }

  @Override
  public void bindData(final @Nullable Object data) throws Exception {
    final Pair<Project, List<ProjectStatsEnvelope.ReferrerStats>> referrerStats = requireNonNull((Pair<Project, List<ProjectStatsEnvelope.ReferrerStats>>) data);
    this.viewModel.inputs.projectAndReferrerStatsInput(referrerStats);
  }

  private void setAmountPledgedTextViewText(final Pair<Project, Integer> projectAndAmount, TextView textview) {
    final String amountString = this.ksCurrency.format(projectAndAmount.second, projectAndAmount.first, false, true, RoundingMode.DOWN);
    textview.setText(amountString);
  }
}