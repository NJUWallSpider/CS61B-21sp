package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static gitlet.Utils.*;
import static gitlet.Utils.serialize;


// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    static final File GITLET_DIR = join(CWD, ".gitlet");
    static final File head = join(GITLET_DIR, "HEAD");
    static final File index = join(GITLET_DIR, "index");
    static final File objects = join(GITLET_DIR, "objects");
    static final File branchHeads = join(GITLET_DIR, "refs");


    /** Initiate .gitlet and its files and folders, make the first commit. */
    public static void initiate() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        head.createNewFile();
        index.createNewFile();
        objects.mkdir();
        branchHeads.mkdir();
        Index initialIndex = new Index();
        Commit firstCommit = new Commit("initial commit");
        String sha_of_commit = Utils.sha1((Object) serialize(firstCommit));
        Commit.saveCommit(sha_of_commit, firstCommit);
        initialIndex.writeIndex();
        String HEAD = "ref: refs/main";
        Utils.writeContents(head, HEAD);
        File mainBranch = join(branchHeads, "main");
        mainBranch.createNewFile();
        writeContents(mainBranch, sha_of_commit);
    }

    /** return current branch file, if detached, return null */
    public static File curBranchRef() {
        String Head = readContentsAsString(head);
        if (Head.startsWith("ref:")) {
            return join(GITLET_DIR, Head.substring(5));
        } else {
            return null;
        }
    }

    /** return SHA_1 of current branch that HEAD points to or HEAD has */
    public static String SHA_1_of_head() {
        String Head = readContentsAsString(head);
        if (Head.startsWith("ref:")) {
            File branchref = join(GITLET_DIR, Head.substring(5));
            return readContentsAsString(branchref);
        } else {
            return Head;
        }
    }

    /** update HEAD pointer to the given SHA-1 value */
    public static void updateHead(String SHA_of_new_commit) {
        String Head = readContentsAsString(head);
        if (Head.startsWith("ref:")) {
            File branchRef = join(GITLET_DIR, Head.substring(5));
            writeContents(branchRef, SHA_of_new_commit);
        } else {
            writeContents(head, SHA_of_new_commit);
        }
    }

    /** return the File to store content according to SHA-1 code */
    public static File SHA2File(String SHA_1) {
        String commitDIR = SHA_1.substring(0, 2);
        String commitFile = SHA_1.substring(2, 40);
        return join(objects, commitDIR, commitFile);
    }

    /** return the Commit object the HEAD reference point to */
    private static Commit getHeadCommit() throws IOException {
        String Head = SHA_1_of_head();
        File commit = SHA2File(Head);
        if (!commit.exists()) {
            throw new RuntimeException("Can not find head commit!");
        }
        return Utils.readObject(commit, Commit.class);
    }

    /** turn the file into bytes according to the given filename */
    private static byte[] file2Bytes(String filename) {
        File file = join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        return readContents(file);
    }

    /** return the SHA-1 code of given bytes */
    private static String bytes2SHA_1(byte[] bytes) {
        return Utils.sha1((Object) bytes);
    }

    /** do add logic */
    public static void add(String filename) throws IOException {
        File file = join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Commit headCommit = getHeadCommit(); // get current commit reference

        // filename -> bytes -> SHA-1
        byte[] fileContent = file2Bytes(filename);
        String curFileRef = bytes2SHA_1(fileContent);

        Index stagingArea = Index.readIndex(); // get the staging area

        stagingArea.stage(filename, curFileRef, fileContent);

        // check if the file is identical to current commit, if so, unstage it
        if (headCommit.hasFile(filename)) {
            String prevFileRef = headCommit.getFile(filename);
            if (prevFileRef.equals(curFileRef)) { // two refs are the same
                stagingArea.unstage(filename);
            }
        }

        stagingArea.writeIndex();
    }


    public static void commit(String message) throws IOException {
        String ref = SHA_1_of_head();
        Commit headCommit = getHeadCommit();
        Index stagingArea = Index.readIndex();
        Commit newCommit = new Commit(message, stagingArea.indexMap, headCommit.file2Blob, ref);
        String SHA_of_new_commit = Utils.sha1((Object) serialize(newCommit));
        Commit.saveCommit(SHA_of_new_commit, newCommit);
        updateHead(SHA_of_new_commit);
        stagingArea.indexMap.clear();
        stagingArea.writeIndex();
    }


    public static void branch(String branName) throws IOException {
        File branchref = join(branchHeads, branName);
        if (branchref.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            branchref.createNewFile();
            String ref = SHA_1_of_head();
            writeContents(branchref, ref);
        }
    }


    public static void status() {
        //deal with branches
        System.out.println("=== Branches ===");
        // get current branch
        String HeadContent = readContentsAsString(head);
        // get all branches
        File[] files = branchHeads.listFiles();
        assert files != null;
        Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.equals(HeadContent.substring(10))) {
                System.out.print('*');
            }
            System.out.println(fileName);
        }
        System.out.print('\n');

        //deal with staged file
        System.out.println("=== Staged Files ===");
        Index index = Index.readIndex();
        Set<String> keys = index.indexMap.keySet();;
        List<String> keyList = new ArrayList<>(keys);;
        if (!index.indexMap.isEmpty()) {
            Collections.sort(keyList);
            for (String key : keyList) {
                System.out.println(key);
            }
        }
        System.out.print('\n');

        //deal with removed files
        System.out.println("=== Removed Files ===");
        File[] workingFiles = CWD.listFiles();
        assert workingFiles != null;
        Arrays.sort(workingFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        Set<String> workingFileNames = new HashSet<>();
        for (File file : workingFiles) {
            if (file.isFile()) {
                String fileName = file.getName();
                workingFileNames.add(fileName);
            }
        }
        if (!index.indexMap.isEmpty()) {
            for (String key : keyList) {
                if (!workingFileNames.contains(key)) {
                    System.out.println(key);
                }
            }
        }
        System.out.print('\n');
    }


    public static void checkoutFile(String fileName) throws IOException {
        checkoutFile(fileName, SHA_1_of_head());
    }


    public static void checkoutFile(String fileName, String id) throws IOException {
        if (id.length() != 40) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        File commitFile = SHA2File(id);
        Commit commit = Commit.readCommit(id, "No commit with that id exists.");

        if (!commit.hasFile(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            File file = join(CWD, fileName);
            String SHA_of_committed_file = commit.getFile(fileName);
            File committedFile = SHA2File(SHA_of_committed_file);
            byte[] fileContents = readContents(committedFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            writeContents(file, (Object) fileContents);
        }

        Index index = Index.readIndex();
        if (index.indexMap.keySet().contains(fileName)) {
            index.unstage(fileName);
        }
        index.writeIndex();
    }


    public static void checkoutBranch(String branchName) throws IOException {
        // get current branch and check
        File curBranch = curBranchRef();
        if (curBranch != null) {
            if (curBranch.getName().equals(branchName)) {
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            }
        }

        // check whether the branch exists and then get the referred commit
        File[] branchFiles = branchHeads.listFiles();
        assert branchFiles != null;
        boolean exists = false;
        File targetBranch = null;
        for (File file : branchFiles) {
            if (file.getName().equals(branchName)) {
                exists = true;
                targetBranch = file;
                break;
            }
        }
        if (!exists) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String SHA_of_commit = readContentsAsString(targetBranch);
        Commit commit = Commit.readCommit(SHA_of_commit, "the branch ref points nowhere.");

        //delete all files in CWD
        File[] workingFiles = CWD.listFiles();
        assert workingFiles != null;
        for (File file : workingFiles) {
            if (file.isFile()) {
                file.delete();
            }
        }

        //clone all files in commit to CWD
        for (String key: commit.file2Blob.keySet()) {
            String sha = commit.file2Blob.get(key);
            File f = join(CWD, key);
            File targetFile = SHA2File(sha);
            byte[] fileContents = readContents(targetFile);
            f.createNewFile();
            writeContents(f, fileContents);
        }

        //change HEAD
        writeObject(head, serialize("ref: refs/" + branchName));

        //clear stage area
        Index index = Index.readIndex();
        index.indexMap.clear();
        index.writeIndex();
    }


    public static void log() throws IOException {
        Commit iterCommit = getHeadCommit();
        String id = SHA_1_of_head();
        while (id != null) {
            System.out.println("===");
            System.out.print("commit " + id + '\n');

            if (iterCommit.secParent() != null) {
                System.out.print("Merge: ");
                System.out.print(iterCommit.parent().substring(0,7) + ' ' + iterCommit.secParent().substring(0,7) + '\n');
            }

            System.out.print("Date: ");
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
            String formattedDate = sdf.format(iterCommit.date());
            System.out.println(formattedDate);

            System.out.println(iterCommit.log());
            System.out.print('\n');

            id = iterCommit.parent();
            if (id != null) {
                iterCommit = Commit.readCommit(id, "parent commit doesn't exists.");
            }
        }

    }

    public static void rm(String arg) {

    }
}
