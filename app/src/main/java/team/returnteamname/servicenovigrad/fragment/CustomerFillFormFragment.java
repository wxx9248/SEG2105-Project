package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;

public class CustomerFillFormFragment extends Fragment
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String[] customerInfo = {"First name","Last Name","DOB","address"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_fill_form,
                                     container, false);
        Bundle bundle = getArguments();

        EditText[] editTextValues = new EditText[4];

        editTextValues[0] = view.findViewById(R.id.editTextCustomerServiceFirstName);
        editTextValues[1] = view.findViewById(R.id.editTextCustomerServiceLastName);
        editTextValues[2] = view.findViewById(R.id.editTextCustomerServiceAddressStreet);
        editTextValues[3] = view.findViewById(R.id.editTextCustomerServicePostalAddress);

        DatePicker   customerDOB              = view.findViewById(R.id.datePickerCustomerDOB);
        LinearLayout linearLayoutCustomerForm = view.findViewById(R.id.linearLayoutCustomerForm);
        Button       buttonSubmit     = view.findViewById(R.id.buttonCustomerFormSubmit);



        String serviceType = (String) bundle.getSerializable("serviceType");


        if (bundle != null)
        {
            CustomerAccount account = (CustomerAccount) bundle.getSerializable("account");

            try{
                buttonSubmit.setOnClickListener(
                    v ->
                    {
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        try{

                            if (editTextValues[0] == null
                                || editTextValues[1] == null
                                || editTextValues[2] == null
                                || editTextValues[3] == null)
                            {
                                Toast.makeText(getContext(), "All fields should be entered",
                                               Toast.LENGTH_SHORT).show();
                            }else{
                                // validate address, postal code, first and last name. date of birth
                                // then send everything to the branch
                                if((editTextValues[0].toString() != account.getFirstName())
                                   || (editTextValues[1].toString() != account.getLastName())){
                                    Toast.makeText(getContext(),
                                                   "Please make sure that you use correct name",
                                                   Toast.LENGTH_SHORT).show();
                                }

                                String customerDofB = customerDOB.getDayOfMonth()+"-"
                                                      +customerDOB.getMonth()+"-"
                                                      +customerDOB.getYear();

                                // after validate store, editTextValues[0-3], customerOofB to database.
                                databaseReference.child("branchServiceRequest").child(account.getUsername())
                                                 .child(serviceType).child("First Name").setValue(editTextValues[0]);
                                databaseReference.child("branchServiceRequest").child(account.getUsername())
                                                 .child(serviceType).child("Last Name").setValue(editTextValues[1]);
                                databaseReference.child("branchServiceRequest").child(account.getUsername())
                                                 .child(serviceType).child("Address").setValue(editTextValues[2]+", "+editTextValues[3]);




                            }



                        }catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(),
                                           Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    });
            }catch (Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }else
            throw new IllegalArgumentException("Invalid argument");


        return view;
    }
}
