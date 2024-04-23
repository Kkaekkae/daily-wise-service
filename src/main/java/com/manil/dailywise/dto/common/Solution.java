import java.util.*;
class Solution {
    public int[] solution(String[] id_list, String[] report, int k) {
        HashMap<String, Integer> reportInfo = new HashMap<>();
        HashMap<String, String[]> reportVerification = new HashMap<>();

        for (String info: report) {
            String[] data = info.split(" ");
            String key = data[0];
            String value = data[1];
            String[] verification = reportVerification.get(key);
            if(Arrays.asList(verification).contains(value)) break;
            reportInfo.putIfAbsent(key, 0);
            verification[verification.length] = value;
            reportVerification.put(key,verification);
            reportInfo.put(key,reportInfo.get(key) + 1);
        }
        int index = 0;

        int[] answer = new int[reportInfo.size()];
        for (String key: reportInfo.keySet() ){
            answer[index] = reportInfo.get(key);
            index++;
        }

        return answer;
    }
}