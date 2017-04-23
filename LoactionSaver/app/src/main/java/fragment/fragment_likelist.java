package fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thekiet.loactionsaver.R;

/**
 * Created by TheKiet on 4/18/2017.
 */

public class fragment_likelist extends Fragment {
    TextView tbao;
    @Nullable
    @Override

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_likelist,container, false);
    }
}
