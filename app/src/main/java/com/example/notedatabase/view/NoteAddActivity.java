package com.example.notedatabase.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.notedatabase.R;
import com.example.notedatabase.noteItem.Note;
import com.example.notedatabase.viewModels.NoteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;


public class NoteAddActivity extends BottomSheetDialogFragment {
    private NoteViewModel viewModel;
    private TextInputEditText textInputTitle;
    private TextInputEditText textInputDescription;
    private TextInputEditText textInputTime;
    private TextInputEditText textInputDate;
    private ChipGroup chipGroup;
    private Button addEditButton;
    private Note note;
    private Chip priority0;
    private Chip priority1;
    private Chip priority2;
    private String TAG;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(NoteViewModel.class);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        textInputTime = view.findViewById(R.id.textTimeSetField);
        textInputDate = view.findViewById(R.id.textDateSetField);
        textInputTitle = view.findViewById(R.id.textTitle);
        textInputDescription = view.findViewById(R.id.textDescription);
        chipGroup = view.findViewById(R.id.priorityCheck);
        addEditButton = view.findViewById(R.id.addEditButton);
        priority0 = view.findViewById(R.id.priority_0);
        priority1 = view.findViewById(R.id.priority_1);
        priority2 = view.findViewById(R.id.priority_2);
        TAG = this.getTag();

        if (TAG.equals("EDIT_NOTE")) {
            textInputTime.setText(note.getTime());
            textInputDate.setText(note.getDate());
            textInputTitle.setText(note.getTitle());
            textInputDescription.setText(note.getDescription());
            setCheck(note);
            addEditButton.setText("Сохранить");
        }
        //Обработчик нажатия на кнопку
        addEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TAG.equals("ADD_NOTE")) {
                    Note addedNote = new Note(textInputTitle.getText().toString(),
                            textInputDescription.getText().toString(),
                            textInputDate.getText().toString(),
                            textInputTime.getText().toString(),
                            getCheckedPriority(chipGroup.getCheckedChipIds().get(0)));

                    viewModel.insertNote(addedNote);
                }
                else if (TAG.equals("EDIT_NOTE")) {
                    note.setTitle(textInputTitle.getText().toString());
                    note.setDescription(textInputDescription.getText().toString());
                    note.setTime(textInputTime.getText().toString());
                    note.setDate(textInputDate.getText().toString());
                    note.setPriority(getCheckedPriority(chipGroup.getCheckedChipId()));
                    viewModel.updateNote(note);
                }

                dismiss();
            }
        });

        //Обработчик установки времени
        textInputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.get(Calendar.HOUR_OF_DAY);
                calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textInputTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });
        //Обработчик установки времени



        //Обработчик установки даты
        textInputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Выберите дату");
                builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                MaterialDatePicker<Long> picker = builder.build();
                assert getFragmentManager() != null;
                picker.show(getFragmentManager(),"TAG");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(selection);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        textInputDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));
                    }
                });
            }
        });
        //Обработчик установки даты

        return view;
    }


    public void setNote(Note note) {
        this.note = note;
    }

//Установить выбранный приоритет в ChipGroup
    private void setCheck(Note note) {
        switch (note.getPriority()) {
            case 0:
                chipGroup.check(priority0.getId());
                break;
            case 1:
                chipGroup.check(priority1.getId());
                break;
            case 2:
                chipGroup.check(priority2.getId());
                break;
        }
    }

//Получить выбранный приоритет из id Chip
    private int getCheckedPriority(int id) {
        if (id == priority0.getId()) {
            return 0;
        }
        if (id == priority1.getId()) {
            return 1;
        }
        if (id == priority2.getId()) {
            return 2;
        }
        return 0;
    }

}
