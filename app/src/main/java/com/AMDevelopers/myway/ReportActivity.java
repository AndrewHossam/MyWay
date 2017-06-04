package com.AMDevelopers.myway;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportActivity extends Fragment {

    Button SendReport;
    EditText ProblemReport;
    Socket socket;
    DataOutputStream send;
    DataInputStream recieve;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Report = inflater.inflate(R.layout.activity_report, container, false);
        ProblemReport = (EditText) Report.findViewById(R.id.Problem);
        SendReport = (Button) Report.findViewById(R.id.Report);
        SendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendReport.setBackgroundColor(0xff79b4d4);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String Problem = ProblemReport.getText().toString();
                            if (Problem.isEmpty() || Problem.equals("")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "No Problem Described ... Please, Enter The Problem", Toast.LENGTH_SHORT).show();
                                        SendReport.setBackgroundColor(0xff67a2bf);
                                    }
                                });
                                return;
                            }
                            String ID = new Encryption().Encrypt(String.valueOf(MainActivity.ID));
                            socket = new Socket("54.187.117.45", 6310);
                            send = new DataOutputStream(socket.getOutputStream());
                            recieve = new DataInputStream(socket.getInputStream());
                            send.writeUTF("REPORT");
                            send.writeUTF(ID);
                            send.writeUTF(Problem);
                            recieve.close();
                            send.close();
                            socket.close();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SendReport.setBackgroundColor(0xff67a2bf);
                                    Toast.makeText(getActivity().getApplicationContext(), "Thank You Very Much ... We Will Work On That", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (final UnknownHostException e) {
                                Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, null, e);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        SendReport.setBackgroundColor(0xff67a2bf);
                                    }
                                });
                        } catch (final IOException e) {
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, null, e);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    SendReport.setBackgroundColor(0xff67a2bf);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        return Report;
    }
}
