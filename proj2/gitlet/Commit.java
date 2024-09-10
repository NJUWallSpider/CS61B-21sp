package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.TreeMap;

import static gitlet.Repository.index;

/** Represents a gitlet commit object.
 *  Generate new commit with given log information, read commit object from file,
 *  compute the SHA-1 of any commit object, write commit object into file.
 *
 *  @author Bai Haozhen
 */
public class Commit implements Serializable {
    /**
     * String message, the log messaage of this commit
     * Date timeStamp, the time when this commit happens
     * TreeMap<String, String> file2Blob, the mapping between file names and blob references
     * String parentRef, a parent reference
     * String secParentRef, a second parent reference for merge
     */

    private String message;
    private Date timeStamp;
    public TreeMap<String, String> file2Blob;
    private String parentRef;
    private String secParentRef;

    public Commit(String message, TreeMap<String, String> staging, TreeMap<String, String> pCommit, String ref) {
        this.message = message;
        timeStamp = new Date();
        file2Blob = updateHead(staging, pCommit);
        parentRef = ref;
        secParentRef = null;
    }

    public Commit(String message) {
        this.message = message;
        timeStamp = new Date(0);
        file2Blob = new TreeMap<>();
        parentRef = null;
        secParentRef = null;
    }

    public static TreeMap<String, String> updateHead(TreeMap<String, String> staging,
                                                     TreeMap<String, String> parent) {
        for (String key : staging.keySet()) {
            String value = staging.get(key);
            parent.put(key, value);
        }
        return parent;
    }

    public boolean hasFile(String filename) {
        return file2Blob.containsKey(filename);
    }

    public String getFile(String filename) {
        return file2Blob.get(filename);
    }

    /** read current Commit obj from .gitlet/objects with given id,
     * if the commit with this id doesn't exist, print cwd and exit. */
    public static Commit readCommit(String SHA_1, String cmd) throws IOException {
        File commit = Repository.SHA2File(SHA_1);
        if (!commit.exists()) {
            System.out.println(cmd);
            System.exit(0);
        }
        return Utils.readObject(commit, Commit.class);
    }


    public static void saveCommit(String SHA_1, Commit obj) throws IOException {
        File commit = Repository.SHA2File(SHA_1);
        if (!commit.exists()) {
            if (!commit.getParentFile().exists()) {
                commit.getParentFile().mkdirs();
            }
            commit.createNewFile();
        }
        Utils.writeObject(commit, obj);
    }

    public String log() {
        return message;
    }

    public Date date() {
        return timeStamp;
    }

    public String parent() {
        return parentRef;
    }

    public String secParent() {
        return secParentRef;
    }
}
