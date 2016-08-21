package fei.tcc.parentalcontrol.component;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.String.format;

/**
 * Created by thiagoretondar on 8/3/16.
 */
public class ForegroundProcessManager {

    private static final String TAG = "ForegroundProcess";

    // fist app user
    public static final int AID_APP = 10000;

    // offset for uid ranges for each user
    public static final int AID_USER = 100000;

    public static String getForegroundApp() {
        // open "/proc" directory file which contains the info we need about process
        File[] files = new File("/proc").listFiles();
        int lowestOomScore = Integer.MAX_VALUE;
        String foregroundProcess = null;

        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }

            int pid;
            try {
                pid = Integer.parseInt(file.getName());
            } catch (NumberFormatException e) {
                // if cannot parse, go to the next file
                continue;
            }

            try {
                String cgroup = read(format("/proc/%d/cgroup", pid));

                String[] lines = cgroup.split("\n");

                // Some devices have lines.length = 3, others are equals 2
                /*if (lines.length != 3) {
                    continue;
                }*/

                String cpuSubsystem = lines[0];
                String cpuaccctSubsystem = lines[lines.length - 1];

                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
                    // not an application process
                    continue;
                }

                if (cpuSubsystem.endsWith("bg_non_interactive")) {
                    // background policy
                    continue;
                }

                String cmdline = read(format("/proc/%d/cmdline", pid)).trim();
                Log.d(TAG, "Process found: " + cmdline);

                if (cmdline.contains("com.android.systemui") ||
                        cmdline.contains("com.mediatek.nlpservice") ||
                        cmdline.contains("com.google.android.googlequicksearchbox:interactor") ||
                        cmdline.contains("android.process.acore") ||
                        cmdline.contains("android.process.media") ||
                        cmdline.contains("com.android.vending") ||
                        cmdline.contains("com.google.android.gms") ||
                        cmdline.contains("com.android.defcontainer") ||
                        cmdline.contains("com.google.process.gapps")) {
                    Log.d(TAG, "Removing " + cmdline + " from the process list");
                    continue;
                }

                int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
                if (uid >= 1000 && uid <= 1038) {
                    // system process
                    continue;
                }

                int appId = uid - AID_APP;
                int userId = 0;
                // loop until we get the correct user id.
                // 100000 is the offset for each user.
                while (appId > AID_USER) {
                    appId -= AID_USER;
                    userId++;
                }

                if (appId < 0) {
                    continue;
                }

                // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
                // String uidName = String.format("u%d_a%d", userId, appId);

                File oomScoreAdj = new File(format("/proc/%d/oom_score_adj", pid));
                if (oomScoreAdj.canRead()) {
                    int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
                    if (oomAdj != 0) {
                        continue;
                    }
                }

                int oomscore = Integer.parseInt(read(format("/proc/%d/oom_score", pid)));
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore;
                    foregroundProcess = cmdline;
                }

            } catch (IOException e) {
                Log.e(TAG, "ERROR WHEN GETTING FOREGROUND PROCCESS", e);
                e.printStackTrace();
            }
        }

        Log.d(TAG, "Foreground proccess found: " + foregroundProcess);
        if (foregroundProcess != null && foregroundProcess.contains(":")) {
            foregroundProcess = foregroundProcess.split(":")[0];
        }
        return foregroundProcess;
    }

    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            output.append('\n').append(line);
        }
        reader.close();
        return output.toString();
    }

}
