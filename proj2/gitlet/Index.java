package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.TreeMap;

import static gitlet.Repository.index;
import static gitlet.Repository.objects;

public class Index implements Serializable {
//    private class IndexEntries {
//        String SHA_1;
//        String fileName;
//        public IndexEntries(String SHA_1, String fileName) {
//            this.SHA_1 = SHA_1;
//            this.fileName = fileName;
//        }
//    }
    public TreeMap<String, String> indexMap = new TreeMap<>();

    /** read current Index obj from .gitlet/index */
    public static Index readIndex() {
        return Utils.readObject(index, Index.class);
    }

    /** write current Index obj into .gitlet/index */
    public void writeIndex() {
        Utils.writeContents(index, (Object) Utils.serialize(this));
    }

    private static void savefile(String SHA_1, byte[] content) throws IOException {
        File file = Repository.SHA2File(SHA_1);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        Utils.writeContents(file, (Object) content);
    }

    /** check if the file has already been staged */
    public boolean check(String filename) {
        return indexMap.containsKey(filename);
    }

    public void stage(String filename, String curFileRef, byte[] fileContent) throws IOException {
        indexMap.put(filename, curFileRef);
        savefile(curFileRef, fileContent);
    }

    public void unstage(String filename) {
        indexMap.remove(filename);
    }
}
