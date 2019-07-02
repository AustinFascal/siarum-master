package com.asramaum.siarum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by austi on 1/27/2019.
 */

public class SettingsBottomSheetDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_settings, container, false);

        MaterialRippleLayout changeThemeButton = v.findViewById(R.id.changeThemeButton);
        MaterialRippleLayout openSourceLicensesButton = v.findViewById(R.id.openSourceLicensesButton);
        MaterialRippleLayout ratingsButton = v.findViewById(R.id.ratingsButton);
        MaterialRippleLayout faqButton = v.findViewById(R.id.faqButton);
        MaterialRippleLayout helpAndFeedbackButton = v.findViewById(R.id.helpAndFeedbackButton);
        MaterialRippleLayout privacyPolicyButton = v.findViewById(R.id.privacyPolicyButton);
        MaterialRippleLayout termsOfServiceButton = v.findViewById(R.id.termsOfServiceButton);

        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        openSourceLicensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ratingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        helpAndFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        termsOfServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialog);
    }
}
