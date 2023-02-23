package com.example.inventorymanagementsystem.views;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.inventorymanagementsystem.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginAndOpenMenuTest {

    @Rule
     public ActivityScenarioRule<Login> mActivityScenarioRule =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void loginAndOpenMenuTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.idEdtLoginEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.idEdtLoginEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("tylerjenningsw@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.idEdtLoginEmail), withText("tylerjenningsw@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.idEdtLoginPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("poop99"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.idEdtLoginPassword), withText("poop99"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.idLoginBtn), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.dialogChooseWarehouseSpinner), withContentDescription("Warehouse"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("WestCoastWarehouse")).inRoot(isPlatformPopup()).perform(click());


        ViewInteraction button = onView(
                allOf(withId(R.id.dialogChooseWarehouseSubmitButton), withText("SUBMIT"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction frameLayout = onView(
                allOf(withId(android.R.id.content),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.dialogChooseWarehouseSubmitButton), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.idMenuHamburguer),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.preference.R.id.action_bar),
                                        0),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Company Details"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Company Details")));

        ViewInteraction textView2 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Users"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("Users")));

        ViewInteraction textView3 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Log out"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("Log out")));

        ViewInteraction textView4 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Products"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("Products")));

        ViewInteraction textView5 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Shipping Orders"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("Shipping Orders")));

        ViewInteraction textView6 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Purchase Orders"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView6.check(matches(withText("Purchase Orders")));

        ViewInteraction textView7 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Receiving"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView7.check(matches(withText("Receiving")));

        ViewInteraction textView8 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Inventory"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView8.check(matches(withText("Inventory")));

        ViewInteraction textView9 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Locations"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView9.check(matches(withText("Locations")));

        ViewInteraction textView10 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Barcode Scanner"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView10.check(matches(withText("Barcode Scanner")));

        ViewInteraction textView11 = onView(
                allOf(withId(androidx.preference.R.id.title), withText("Utilities"),
                        withParent(withParent(withId(androidx.preference.R.id.content))),
                        isDisplayed()));
        textView11.check(matches(withText("Utilities")));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mActivityScenarioRule.getScenario().close();


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
