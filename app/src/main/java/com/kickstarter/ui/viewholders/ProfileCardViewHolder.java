package com.kickstarter.ui.viewholders;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kickstarter.R;
import com.kickstarter.models.Project;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.ButterKnife;

public class ProfileCardViewHolder extends KSViewHolder {
  private final Delegate delegate;
  protected Project project;

  protected @Bind(R.id.funding_unsuccessful_text_view) TextView fundingUnsuccessfulTextView;
  protected @Bind(R.id.percentage_funded) ProgressBar percentageFundedProgressBar;
  protected @Bind(R.id.profile_card_image) ImageView profileCardImageView;
  protected @Bind(R.id.profile_card_name) TextView profileCardNameTextView;
  protected @Bind(R.id.project_state_view_group) ViewGroup projectStateViewGroup;
  protected @Bind(R.id.successfully_funded_text_view) TextView successfullyFundedTextView;

  protected @BindDrawable(R.drawable.gray_gradient) Drawable grayGradientDrawable;

  protected @BindString(R.string.___Successful) String successfulString;
  protected @BindString(R.string.___Unsuccessful) String unsuccessfulString;
  protected @BindString(R.string.___Cancelled) String cancelledString;
  protected @BindString(R.string.___Suspended) String suspendedString;

  public interface Delegate {
    void projectCardClick(ProfileCardViewHolder viewHolder, Project project);
  }

  public ProfileCardViewHolder(final @NonNull View view, final @NonNull Delegate delegate) {
    super(view);
    this.delegate = delegate;
    ButterKnife.bind(this, view);
  }

  @Override
  public void onBind(final @NonNull Object datum) {
    this.project = (Project) datum;

    Picasso.with(view.getContext()).load(project.photo().med())
      .placeholder(grayGradientDrawable)
      .into(profileCardImageView);
    profileCardNameTextView.setText(project.name());
    percentageFundedProgressBar.setProgress(Math.round(Math.min(100.0f, project.percentageFunded())));

    setProjectStateView();
  }

  @Override
  public void onClick(@NonNull final View view) {
    delegate.projectCardClick(this, project);
  }

  public void setProjectStateView() {
    switch(project.state()) {
      case Project.STATE_SUCCESSFUL:
        percentageFundedProgressBar.setVisibility(View.GONE);
        projectStateViewGroup.setVisibility(View.VISIBLE);
        fundingUnsuccessfulTextView.setVisibility(View.GONE);
        successfullyFundedTextView.setVisibility(View.VISIBLE);
        successfullyFundedTextView.setText(successfulString);
        break;
      case Project.STATE_CANCELED:
        percentageFundedProgressBar.setVisibility(View.GONE);
        projectStateViewGroup.setVisibility(View.VISIBLE);
        successfullyFundedTextView.setVisibility(View.GONE);

        fundingUnsuccessfulTextView.setVisibility(View.VISIBLE);
        fundingUnsuccessfulTextView.setText(cancelledString);
        break;
      case Project.STATE_FAILED:
        percentageFundedProgressBar.setVisibility(View.GONE);
        projectStateViewGroup.setVisibility(View.VISIBLE);
        successfullyFundedTextView.setVisibility(View.GONE);
        fundingUnsuccessfulTextView.setVisibility(View.VISIBLE);
        fundingUnsuccessfulTextView.setText(unsuccessfulString);
        break;
      case Project.STATE_SUSPENDED:
        percentageFundedProgressBar.setVisibility(View.GONE);
        projectStateViewGroup.setVisibility(View.VISIBLE);
        successfullyFundedTextView.setVisibility(View.GONE);
        fundingUnsuccessfulTextView.setVisibility(View.VISIBLE);
        fundingUnsuccessfulTextView.setText(suspendedString);
        break;
    }
  }
}
