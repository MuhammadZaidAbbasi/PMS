package ChatAndVideo;

import java.util.UUID;

    public class VideoCall {
        private String meetingLink;

        public VideoCall() {
            this.meetingLink = "https://meet.jit.si/" + UUID.randomUUID().toString();
        }

        public String getMeetingLink() {
            return meetingLink;
        }

        // Display method
        public void displayMeetingLink() {
            System.out.println("Join the video call using this link: " + meetingLink);
        }
    }




